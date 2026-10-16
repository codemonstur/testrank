package testrank.core;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateReport {

    public static void generateReport(final String html) throws IOException {
        try (final var output = new FileOutputStream("report.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.withHtmlContent(html, null);
            builder.toStream(output);
            builder.run();
        }
    }

}
