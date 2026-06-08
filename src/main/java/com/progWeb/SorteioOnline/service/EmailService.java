package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.config.ResendConfig;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final Resend resend;

    @Value("${email.from}")
    private String emailFrom;

    @Value("${app.base.url}")
    private String appBaseUrl;

    public EmailService(Resend resend) {
        this.resend = resend;
    }

    public void enviarEmailVencedor(String emailVencedor, String usuarioVencedor, Long numeroSorteio, String nomeSorteio) {
        String html = "<div style=\"font-family: sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto;\">\n" +
                "    <h2>Olá, " + usuarioVencedor + "!</h2>\n" +
                "    <p>Temos uma notícia incrível para você: <strong>você foi o grande vencedor do nosso sorteio!</strong> \uD83E\uDD73</p>\n" +
                "    \n" +
                "    <p>Aqui estão os detalhes da sua vitória:</p>\n" +
                "    <table style=\"width: 100%; border-collapse: collapse; margin: 20px 0;\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 8px; border-bottom: 1px solid #eee;\"><strong>Sorteio:</strong></td>\n" +
                "            <td style=\"padding: 8px; border-bottom: 1px solid #eee;\">#" + numeroSorteio + "</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 8px; border-bottom: 1px solid #eee;\"><strong>Prêmio:</strong></td>\n" +
                "            <td style=\"padding: 8px; border-bottom: 1px solid #eee;\">" + nomeSorteio + "</td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <p>Para resgatar o seu prêmio ou ver mais detalhes, basta acessar a sua conta na nossa plataforma clicando no botão abaixo:</p>\n" +
                "    \n" +
                "    <p style=\"margin: 30px 0; text-align: center;\">\n" +
                "        <a href=\"" + appBaseUrl + numeroSorteio + "\"\n" +
                "           style=\"background-color: #2563eb; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; font-weight: bold; display: inline-block;\">\n" +
                "           Acessar Meu Prêmio\n" +
                "        </a>\n" +
                "    </p>\n" +
                "\n" +
                "    <hr style=\"border: 0; border-top: 1px solid #eee; margin: 30px 0;\" />\n" +
                "    <p style=\"font-size: 0.9em; color: #666;\">Parabéns mais uma vez,<br /><strong>Equipe Sorteio Online</strong></p>\n" +
                "</div>";

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(emailFrom)
                .to(emailVencedor)
                .subject("Resultado do sorteio #" + numeroSorteio)
                .html(html)
                .build();

        try {
            CreateEmailResponse response = resend.emails().send(params);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
