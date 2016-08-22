package de.rwthaachen.cbmb.Configuration;

import org.ocpsoft.rewrite.annotation.RewriteConfiguration;
import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

import javax.servlet.ServletContext;

@RewriteConfiguration
public class FacesRewriteConfigurationProvider extends HttpConfigurationProvider {

    @Override
    public int priority()
    {
        return 10;
    }

    @Override
    public Configuration getConfiguration(final ServletContext context)
    {
        return ConfigurationBuilder.begin()
                .addRule(Join.path("/").to("/index.jsf"))
                .addRule(Join.path("/index").to("/index.jsf"))
                .addRule(Join.path("/index.xhtml").to("/index.jsf"))
                .addRule(Join.path("/files").to("/file.jsf"))
                .addRule(Join.path("/login").to("/login.jsf"))
                .addRule(Join.path("/logout").to("/logout.jsf"))
                .addRule(Join.path("/users").to("/users.jsf"))
                .addRule(Join.path("/register").to("/register.jsf"));
    }
}
