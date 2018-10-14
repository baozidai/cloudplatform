package team.cloud.platform.swagger;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Ernest
 * @date 2018/9/26下午1:14
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = "team.cloud.platform.controller")
public class SwaggerConfig {

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("云平台[Api接口文档]")
                .description("")
                .contact(new Contact("Mr.lu", "http://154882049.cn", "ljw9712@163.com"))
                .version("1.0")
                .licenseUrl("http://localhost:8080/v2/api-docs")
                .build();
        docket.apiInfo(apiInfo);
        //设置只生成被Api这个注解注解过的Ctrl类中有ApiOperation注解的api接口的文档
        docket.select().apis(RequestHandlerSelectors.withClassAnnotation(Api.class)).apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
        return docket;
    }
}

