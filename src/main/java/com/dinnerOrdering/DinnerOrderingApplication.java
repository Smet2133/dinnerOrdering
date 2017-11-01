package com.dinnerOrdering;

import com.dinnerOrdering.config.DriverManagerMy;
import com.dinnerOrdering.db.DbController;
import com.dinnerOrdering.scheduling.Scheduling;
import com.dinnerOrdering.security.VaadinSessionSecurityContextHolderStrategy;
import com.vaadin.event.dd.acceptcriteria.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.Notification;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ServletComponentScan
//@ServletComponentScan - what for
//Vova has extends SpringBootServletInitializer
public class DinnerOrderingApplication  extends  SpringBootServletInitializer{



	@Autowired
	DbController db;

	@Configuration
	@EnableGlobalMethodSecurity(securedEnabled = true)
	public static class SecurityConfiguration extends GlobalMethodSecurityConfiguration {

		@Autowired
		UserDetailsService userDetailsService;



		@Autowired
		public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		//	auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
			auth.userDetailsService(userDetailsService);

		}

		@Bean(name="passwordEncoder")
		public PasswordEncoder passwordencoder(){
			return new BCryptPasswordEncoder();
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return authenticationManager();
		}

		static {
			// Use a custom SecurityContextHolderStrategy
			SecurityContextHolder.setStrategyName(VaadinSessionSecurityContextHolderStrategy.class.getName());
		}
	}

	public static void main(String[] args) {
		/*Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				test();
			}
		}, 1000L, 10000000000L);
		new DbController(new DriverManagerMy().dataSource()).sendToDbTest();*/

	//	DbController db = new DbController(new DriverManagerMy().dataSource());
	//	db.addAutoupdatingOrders();
	//	File f = new File(System.getProperty("catalina.base") +				File.separator + "webapps" + File.separator + "workbook.xls");
	//	File f = new File ("C:/workbooks/workbook.xls");


		//File errOut1 = new File("/outp1.txt");
/*
		File errOut = new File("/opt/tomcat/webapps/outp.txt");
		File inTomcat = new File("/opt/tomcat/webapps/workbooks/workbook.xls");
		//f.getParentFile().mkdirs();
		try {
		//	f.createNewFile();
			errOut.getParentFile().mkdirs();
			inTomcat.getParentFile().mkdirs();
			errOut.createNewFile();
			inTomcat.createNewFile();
		//	errOut1.createNewFile();
		//	OutputStream output = new FileOutputStream(errOut);
		//	PrintStream printOut = new PrintStream(output);
		//	System.setErr(printOut);
		} catch (IOException e) {
			e.printStackTrace();
		}*/

		//new Scheduling().startScheduling();

		SpringApplication.run(DinnerOrderingApplication.class, args);

	}




	private static Class<DinnerOrderingApplication> applicationClass = DinnerOrderingApplication.class;
/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}
*/

}
