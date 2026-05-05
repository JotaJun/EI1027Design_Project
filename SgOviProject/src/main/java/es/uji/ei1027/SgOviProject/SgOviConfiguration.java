package es.uji.ei1027.SgOviProject;

import es.uji.ei1027.SgOviProject.enums.AccountType;
import es.uji.ei1027.SgOviProject.interceptor.AuthInterceptor;
import es.uji.ei1027.SgOviProject.interceptor.RoleInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    // Configuración para mapear rutas que empiecen con /contracts al directorio correspondiente
    // Inyectamos la ruta de application.properties
    @Value("${upload.directorio.contratos}")
    private String uploadDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Convertimos la ruta relativa a una ruta absoluta que entienda el navegador
        String absolutePath = Paths.get(uploadDirectory).toFile().getAbsolutePath();

        // Mapeamos la URL web a la ruta del disco duro
        registry.addResourceHandler("/contracts/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }

    // Formato fechas estándar mediante formateador
    @Bean
    public Formatter<LocalDate> localDateFormatterISO() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ISO_LOCAL_DATE.format(object);
            }
        };
    }

    // Este método existe para especificar los htmls que necesiten excluyan a ciertos tipos de cuenta
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Interceptor para autenticaciones
        registry.addInterceptor(new AuthInterceptor())
                // Le decimos que vigile TODAS las rutas por defecto
                .addPathPatterns("/**")
                // EXCEPCIONES: Rutas que NO necesitan estar logueados
                .excludePathPatterns(
                        "/",
                        "/index",           // La página de inicio pública
                        "/login",           // Si no, el redireccionamiento hace un bucle
                        "/logout",
                        "/register",
                        "/register/**",     // Para rutas como /register/done
                        "/oviUser/register",
                        "/papPati/register",
                        "/legalGuardian/register",
                        "/css/**",          // Archivos de estilos
                        "/images/**",       // Imágenes
                        "/account/**"
                );

        // Interceptor para roles

        // /** significa esta ruta y cualquier sub-ruta que cuelgue de ella

        // OVIUSER
        registry.addInterceptor(new RoleInterceptor(AccountType.OVIUSER))
                .addPathPatterns(
                        "/oviUser/**",
                        "/assistanceRequest/add",       // Rutas específica
                        "/assistanceRequest/list/**",
                        "/assistanceRequest/details/**",
                        "/assistanceRequest/update/**",
                        "/assistanceRequest/delete/**",
                        "/candidacy/listCandidates/**",
                        "/candidacy/details/**",
                        "/candidacy/reject/**",
                        "/contract/add/**",
                        "/contract/list/**",
                        "/contract/details/**"
                );
        // TECHNICIAN
        registry.addInterceptor(new RoleInterceptor(AccountType.TECHNICIAN))
                .addPathPatterns(
                        "/technician/**" // faltan por poner; por ejemplo las de assistance request
                );

        // PAPPATI
        registry.addInterceptor(new RoleInterceptor(AccountType.PAPPATI))
                .addPathPatterns(
                        "/papPati/**",
                        "/candidacy/listRequests/**"
                );

        // LEGALGUARDIAN
        registry.addInterceptor(new RoleInterceptor(AccountType.LEGALGUARDIAN))
                .addPathPatterns(
                        "/legalGuardian/**"
                );
    }
}