package br.com.ifba.boleto.service;

import br.com.ifba.boleto.entity.BoletosPrefeitura;
import br.com.ifba.boleto.repository.BoletosPrefeituraRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author eduardo
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoletosPrefeituraService implements BoletosPrefeituraIService {

    private final BoletosPrefeituraRepository boletoRepository;

    private static final Logger log = LoggerFactory.getLogger(BoletosPrefeituraService.class);

    @Override
    public BoletosPrefeitura salvar(BoletosPrefeitura boleto) {
        log.info("Iniciando o processo de salvar BoletosPrefeitura.");
        if (boleto.getNumeroCodigoBarras() == null || boleto.getNumeroCodigoBarras().isBlank()) {
            StringBuilder codigoBarras = new StringBuilder();
            for (int j = 0; j < 44; j++) {
                codigoBarras.append((int) (Math.random() * 10));
            }
            boleto.setNumeroCodigoBarras(codigoBarras.toString());
        }
        if (boleto.getStatus() == null || boleto.getStatus().isBlank()) {
            boleto.setStatus("PENDENTE");
        }
        validarStatus(boleto.getStatus());
        if (boleto.getAtivo() == null) {
            boleto.setAtivo(true);
        }
        return boletoRepository.save(boleto);
    }

    @Override
    public BoletosPrefeitura atualizar(Long id, BoletosPrefeitura dadosNovos) {
        log.info("Iniciando processo de atualização do Boleto ID: {}", id);
        BoletosPrefeitura boletoExistente = buscarPorId(id);
        if (dadosNovos.getValorBoleto() != null) {
            boletoExistente.setValorBoleto(dadosNovos.getValorBoleto());
        }
        if (dadosNovos.getDataVencimento() != null) {
            boletoExistente.setDataVencimento(dadosNovos.getDataVencimento());
        }
        if (dadosNovos.getStatus() != null && !dadosNovos.getStatus().isBlank()) {
            validarStatus(dadosNovos.getStatus());
            boletoExistente.setStatus(dadosNovos.getStatus());
        }
        return boletoRepository.save(boletoExistente);
    }

    //busca por ID
    @Override
    public BoletosPrefeitura buscarPorId(Long id) {
        log.info("Buscando BoletosPrefeitura ID: {}", id);
        return boletoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Boleto não encontrado no banco de dados."));
    }

    // Busca por ativo
    @Override
    public List<BoletosPrefeitura> listarAtivos() {
        log.info("Listando todos os Boletos ativos.");
        return boletoRepository.findByAtivoTrue();
    }

    //busca por lancamento
    @Override
    public List<BoletosPrefeitura> listarPorLancamento(Long lancamentoId) {
        log.info("Listando boletos do LancamentoImposto ID: {}", lancamentoId);
        return boletoRepository.findByLancamentoImpostoId(lancamentoId);
    }

    // busca por pendente
    @Override
    public List<BoletosPrefeitura> listarPendentes() {
        log.info("Listando boletos com status PENDENTE.");
        return boletoRepository.findByStatusAndAtivoTrue("PENDENTE");
    }

    // atualizar status
    @Override
    public BoletosPrefeitura atualizarStatus(Long id, String novoStatus) {
        log.info("Atualizando status do Boleto ID {} para '{}'.", id, novoStatus);

        validarStatus(novoStatus);

        BoletosPrefeitura boleto = buscarPorId(id);

        if ("PAGO".equals(boleto.getStatus()) && "PENDENTE".equals(novoStatus)) {
            throw new IllegalArgumentException(
                    "Boleto já pago. Para revertê-lo utilize o estorno de pagamento.");
        }

        boleto.setStatus(novoStatus);
        return boletoRepository.save(boleto);
    }

    // deletar
    @Override
    public void inativar(Long id) {
        log.info("Inativando BoletosPrefeitura ID: {}", id);

        if (id == null || id <= 0) {
            log.error("Tentativa de deletar com ID nulo ou inválido.");
            throw new IllegalArgumentException("O ID do boleto é inválido.");
        }

        BoletosPrefeitura boleto = buscarPorId(id);

        if ("PAGO".equals(boleto.getStatus())) {
            throw new IllegalArgumentException(
                    "Não é possível excluir um boleto já pago. "
                    + "Utilize o estorno de pagamento.");
        }

        boleto.inativar();
        boletoRepository.save(boleto);
        log.info("BoletosPrefeitura ID {} inativado com sucesso.", id);
    }

    //validar status
    private void validarStatus(String status) {
        if (!"PENDENTE".equals(status)
                && !"PAGO".equals(status)
                && !"VENCIDO".equals(status)) {
            throw new IllegalArgumentException(
                    "Status inválido. Valores aceitos: PENDENTE, PAGO, VENCIDO.");
        }
    }
}
