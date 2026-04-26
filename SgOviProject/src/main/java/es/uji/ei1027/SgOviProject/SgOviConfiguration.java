package es.uji.ei1027.SgOviProject;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.interceptor.RoleInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

@Configuration
public class SgOviConfiguration implements WebMvcConfigurer {

    // Configura l'accés a la base de dades (DataSource)
    // a partir de les propietats a src/main/resources/applications.properties
    // que comencen pel prefix spring.datasource
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    // Este método existe para especificar los htmls que necesiten excluyan a ciertos tipos de cuenta
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // /** significa esta ruta y cualquier sub-ruta que cuelgue de ella

        // OVIUSER
        registry.addInterceptor(new RoleInterceptor(AccountType.OVIUSER))
                .addPathPatterns(
                        "/oviUser/**",
                        "/assistanceRequest/add",       // Ruta específica
                        "/assistanceRequest/list"       // Ruta específica
                );

        // TECHNICIAN
        registry.addInterceptor(new RoleInterceptor(AccountType.TECHNICIAN))
                .addPathPatterns(
                        "/technician/**" // faltan por poner; por ejemplo las de assistance request
                );

        // PAPPATI
        registry.addInterceptor(new RoleInterceptor(AccountType.PAPPATI))
                .addPathPatterns(
                        "/papPati/**"
                );

        // LEGALGUARDIAN
        registry.addInterceptor(new RoleInterceptor(AccountType.LEGALGUARDIAN))
                .addPathPatterns(
                        "/legalGuardian/**"
                );
    }
}