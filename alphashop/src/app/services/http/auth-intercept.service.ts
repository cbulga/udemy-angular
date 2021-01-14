import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthJWTService } from '../auth-jwt.service';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor(private authService: AuthJWTService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
/*
    let userId = "Admin";
    let password = "VerySecretPwd";

    let authHeader = "Basic " + window.btoa(userId + ":" + password);
*/
    let authToken = this.authService.getAuthToken();
    let loggedUser = this.authService.loggedUser();

    if (authToken && loggedUser) {
      request = request.clone(
        {
          setHeaders : { Authorization: authToken }
        });
    }

    return next.handle(request);
  }
}
