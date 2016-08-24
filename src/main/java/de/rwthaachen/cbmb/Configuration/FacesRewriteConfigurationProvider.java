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
                .addRule(Join.path("/").to("/views/index.jsf"))
                .addRule(Join.path("/index").to("/views/index.jsf"))
                .addRule(Join.path("/index.xhtml").to("/views/index.jsf"))
                .addRule(Join.path("/files").to("/views/scannedFiles/files.jsf"))
                .addRule(Join.path("/login").to("/views/login.jsf"))
                .addRule(Join.path("/logout").to("/views/logout.jsf"))
                .addRule(Join.path("/users").to("/views/user/users.jsf"))
                .addRule(Join.path("/users/changePassword").to("/views/user/changePassword.jsf"))
                .addRule(Join.path("/users/profile").to("/views/user/profile.jsf"))
                .addRule(Join.path("/register").to("/views/register.jsf"));
    }
}
