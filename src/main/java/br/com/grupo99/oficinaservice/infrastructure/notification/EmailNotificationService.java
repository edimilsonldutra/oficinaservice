package br.com.grupo99.oficinaservice.infrastructure.notification;

import br.com.grupo99.oficinaservice.application.service.NotificationService;
import br.com.grupo99.oficinaservice.domain.model.OrdemServico;
import br.com.grupo99.oficinaservice.domain.repository.ClienteRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;
    private final ClienteRepository clienteRepository;

    public EmailNotificationService(JavaMailSender mailSender, ClienteRepository clienteRepository) {
        this.mailSender = mailSender;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void notificarClienteParaAprovacao(OrdemServico os) {
        // Busca o cliente para obter o e-mail
        clienteRepository.findById(os.getClienteId()).ifPresent(cliente -> {
            if (cliente.getEmail() != null && !cliente.getEmail().isBlank()) {
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom("nao-responda@oficina.com");
                    message.setTo(cliente.getEmail());
                    message.setSubject("Orçamento da sua Ordem de Serviço #" + os.getId().toString().substring(0, 8));

                    String texto = String.format(
                            "Olá, %s!\n\n" +
                                    "O orçamento para a sua Ordem de Serviço está pronto para aprovação.\n" +
                                    "Valor Total: R$ %.2f\n\n" +
                                    "Por favor, entre em contato para aprovar o serviço.\n\n" +
                                    "Atenciosamente,\nEquipe da Oficina",
                            cliente.getNome(),
                            os.getValorTotal()
                    );

                    message.setText(texto);
                    mailSender.send(message);
                    System.out.println("E-mail de orçamento enviado para: " + cliente.getEmail());
                } catch (Exception e) {
                    // Em um projeto real, aqui teríamos um log mais robusto (ex: SLF4J)
                    System.err.println("Erro ao enviar e-mail de orçamento: " + e.getMessage());
                }
            }
        });
    }
}