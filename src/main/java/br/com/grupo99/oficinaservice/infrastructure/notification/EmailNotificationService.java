package br.com.grupo99.oficinaservice.infrastructure.notification;

import br.com.grupo99.oficinaservice.application.service.NotificationService;
import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService implements NotificationService {

    private static final String EMAIL_REMETENTE = "nao-responda@oficina.com";
    private static final String ASSUNTO_TEMPLATE = "Orçamento da sua Ordem de Serviço #%s";
    private static final String MENSAGEM_TEMPLATE = """
            Olá, %s!

            O orçamento para a sua Ordem de Serviço está pronto para aprovação.
            Valor Total: R$ %.2f

            Por favor, entre em contato para aprovar o serviço.

            Atenciosamente,
            Equipe da Oficina
            """;

    private final JavaMailSender mailSender;
    private final ClienteRepository clienteRepository;

    public EmailNotificationService(JavaMailSender mailSender, ClienteRepository clienteRepository) {
        this.mailSender = mailSender;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void notificarClienteParaAprovacao(OrdemServico ordemServico) {
        clienteRepository.findById(ordemServico.getClienteId()).ifPresent(cliente -> {
            if (isEmailValido(cliente.getEmail())) {
                try {
                    SimpleMailMessage mensagem = criarMensagemEmail(cliente.getNome(), cliente.getEmail(), ordemServico);
                    mailSender.send(mensagem);
                    log.info("E-mail de orçamento enviado com sucesso - OS: {}, Cliente: {}", ordemServico.getId(), cliente.getEmail());
                } catch (Exception e) {
                    log.error("Erro ao enviar e-mail - OS: {}, Cliente: {}, Erro: {}", ordemServico.getId(), cliente.getEmail(), e.getMessage(), e);
                }
            } else {
                log.warn("E-mail inválido ou ausente - Cliente: {}, OS: {}", cliente.getId(), ordemServico.getId());
            }
        });
    }

    private boolean isEmailValido(String email) {
        return email != null && !email.isBlank();
    }

    private SimpleMailMessage criarMensagemEmail(String nomeCliente, String email, OrdemServico os) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(EMAIL_REMETENTE);
        message.setTo(email);
        message.setSubject(String.format(ASSUNTO_TEMPLATE, os.getId().toString().substring(0, 8)));
        message.setText(String.format(MENSAGEM_TEMPLATE, nomeCliente, os.getValorTotal()));
        return message;
    }
}