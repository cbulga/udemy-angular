import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';
import { ArticoliComponent } from './articoli/articoli.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { JumbotronComponent } from './jumbotron/jumbotron.component';
import { LogoutComponent } from './logout/logout.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { NewartComponent } from './newart/newart.component';
import { AuthInterceptorService } from './services/http/auth-intercept.service';
import { ClientiComponent } from './clienti/clienti.component';
import { InfoclientiComponent } from './clienti/infoclienti/infoclienti.component';
import { CopyrightDirective } from './directives/copyright.directive';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    LoginComponent,
    ErrorComponent,
    ArticoliComponent,
    NavbarComponent,
    FooterComponent,
    JumbotronComponent,
    LogoutComponent,
    NewartComponent,
    ClientiComponent,
    InfoclientiComponent,
    CopyrightDirective
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgxPaginationModule
  ],
  providers: [{
      provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
