package br.com.ifba.pagamento.service;
import br.com.ifba.boleto.entity.BoletosPrefeitura;
import br.com.ifba.boleto.repository.BoletosPrefeituraRepository;
import br.com.ifba.pagamento.entity.Pagamento;
import br.com.ifba.pagamento.repository.PagamentoRepository;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repository.UsuarioRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação das regras de negócio para Pagamento.
 *
 * @author Sistema de Tributos
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoService implements PagamentoIService {

    private final PagamentoRepository         pagamentoRepository;
    private final BoletosPrefeituraRepository boletoRepository;
    private final UsuarioRepository           usuarioRepository;

    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);

    // -------------------------------------------------------------------------
    // SAVE (baixa manual de boleto)
    // -------------------------------------------------------------------------
    @Override
    public Pagamento salvar(Long boletoId, Long usuarioId) {
        log.info("Processando baixa manual. BoletoID: {}, UsuarioID: {}", boletoId, usuarioId);

        BoletosPrefeitura boleto = boletoRepository.findById(boletoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Boleto não encontrado no banco de dados."));

        if (pagamentoRepository.existsByBoletoPrefeituraIdAndAtivoTrue(boletoId)) {
            throw new IllegalArgumentException(
                    "Este boleto já possui um pagamento ativo registrado.");
        }

        if ("PAGO".equals(boleto.getStatus())) {
            throw new IllegalArgumentException(
                    "Este boleto já está marcado como PAGO.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário operador não encontrado no banco de dados."));

        Pagamento pagamento = new Pagamento();
        pagamento.processarBaixaManual(boleto, usuario);

        boletoRepository.save(boleto);

        Pagamento salvo = pagamentoRepository.save(pagamento);
        log.info("Pagamento ID {} registrado com sucesso para o Boleto ID {}.",
                salvo.getId(), boletoId);
        return salvo;
    }

    @Override
    public Pagamento atualizar(Long id, Pagamento dadosNovos) {
        log.info("Iniciando processo de atualização do Pagamento ID: {}", id);
        Pagamento pagamentoExistente = buscarPorId(id);

        Long boletoId = dadosNovos.getBoletoPrefeitura().getId();
        BoletosPrefeitura boletoNovo = boletoRepository.findById(boletoId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Boleto não encontrado no banco de dados."));
        Long usuarioId = dadosNovos.getUsuario().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário operador não encontrado no banco de dados."));

        if (Boolean.FALSE.equals(pagamentoExistente.getAtivo())) {
            throw new IllegalArgumentException("Pagamento não encontrado no banco de dados.");
        }

        BoletosPrefeitura boletoAntigo = pagamentoExistente.getBoletoPrefeitura();
        Long boletoAntigoId = boletoAntigo == null ? null : boletoAntigo.getId();

        if (boletoId.equals(boletoAntigoId)) {
            if (!usuarioId.equals(pagamentoExistente.getUsuario().getId())) {
                pagamentoExistente.setUsuario(usuario);
                return pagamentoRepository.save(pagamentoExistente);
            }
            return pagamentoExistente;
        }

        if (pagamentoRepository.existsByBoletoPrefeituraIdAndAtivoTrue(boletoId)) {
            throw new IllegalArgumentException(
                    "Este boleto já possui um pagamento ativo registrado.");
        }
        if ("PAGO".equals(boletoNovo.getStatus())) {
            throw new IllegalArgumentException(
                    "Este boleto já está marcado como PAGO.");
        }

        if (boletoAntigo != null) {
            boletoAntigo.estornar();
            boletoRepository.save(boletoAntigo);
        }

        pagamentoExistente.processarBaixaManual(boletoNovo, usuario);
        boletoRepository.save(boletoNovo);
        return pagamentoRepository.save(pagamentoExistente);
    }

    // -------------------------------------------------------------------------
    // FIND BY ID
    // -------------------------------------------------------------------------
    @Override
    public Pagamento buscarPorId(Long id) {
        log.info("Buscando Pagamento ID: {}", id);
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pagamento não encontrado no banco de dados."));
    }

    // -------------------------------------------------------------------------
    // FIND BY ATIVO TRUE
    // -------------------------------------------------------------------------
    @Override
    public List<Pagamento> listarAtivos() {
        log.info("Listando todos os Pagamentos ativos.");
        return pagamentoRepository.findByAtivoTrue();
    }

    // -------------------------------------------------------------------------
    // FIND BY USUARIO
    // -------------------------------------------------------------------------
    @Override
    public List<Pagamento> listarPorUsuario(Long usuarioId) {
        log.info("Listando pagamentos do Usuário ID: {}", usuarioId);
        return pagamentoRepository.findByUsuarioIdAndAtivoTrue(usuarioId);
    }

    // -------------------------------------------------------------------------
    // DELETE — Estorno
    // -------------------------------------------------------------------------
    @Override
    public void estornar(Long id) {
        log.info("Iniciando estorno do Pagamento ID: {}", id);

        if (id == null || id <= 0) {
            log.error("Tentativa de estornar com ID nulo ou inválido.");
            throw new IllegalArgumentException("O ID do pagamento é inválido.");
        }

        Pagamento pagamento = buscarPorId(id);

        if ("ESTORNADO".equals(pagamento.getStatus())) {
            throw new IllegalArgumentException(
                    "Este pagamento já foi estornado anteriormente.");
        }

        pagamento.estornar();
        boletoRepository.save(pagamento.getBoletoPrefeitura());
        pagamentoRepository.save(pagamento);
        log.info("Pagamento ID {} estornado. Boleto ID {} revertido para PENDENTE.",
                id, pagamento.getBoletoPrefeitura() != null ? pagamento.getBoletoPrefeitura().getId() : null);
    }
}
