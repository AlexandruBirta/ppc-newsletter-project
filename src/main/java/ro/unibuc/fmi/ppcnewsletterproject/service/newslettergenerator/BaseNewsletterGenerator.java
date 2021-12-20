package ro.unibuc.fmi.ppcnewsletterproject.service.newslettergenerator;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.compiler.CompiledST;
import ro.unibuc.fmi.ppcnewsletterproject.exception.GenerateEmailException;
import ro.unibuc.fmi.ppcnewsletterproject.model.Account;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public abstract class BaseNewsletterGenerator {
    protected Account account;

    public BaseNewsletterGenerator(Account account) {
        this.account = account;
    }

    abstract protected Path getTemplatePath() throws IOException;

    abstract protected Map<String, String> getTemplateParameters() throws GenerateEmailException;

//    private static final STGroup templates = new STGroup('$', '$');

    protected String getTemplate() throws IOException {
        return new String(Files.readAllBytes(getTemplatePath()));
    }

    protected String renderTemplate() throws GenerateEmailException, IOException {
        STGroup templates = new STGroup('$', '$');
        CompiledST compiledTemplate = templates.defineTemplate("template", getTemplate());
        compiledTemplate.hasFormalArgs = false;

//        if (templates.lookupTemplate("bla") == null) {
//            CompiledST compiledTemplate = templates.defineTemplate("bla", getTemplate());
//            compiledTemplate.hasFormalArgs = false;
//        }

        ST template = templates.getInstanceOf("template");

        getTemplateParameters().forEach(template::add);

        return template.render();
    }

    public String generateRandom() throws GenerateEmailException, IOException {
        return renderTemplate();
    }
}
