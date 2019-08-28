package util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class    PageGenerator {

    private static PageGenerator pageGenerator;

    private static final String HTML_DIR = "templates";

    private final Configuration cfg;

    public static PageGenerator getInstance() {
        if (pageGenerator == null) {
            pageGenerator = new PageGenerator();
        }
        return pageGenerator;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    public PageGenerator() {
        cfg = new Configuration();
    }
}
