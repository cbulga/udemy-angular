import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthInterceptorService implements HttpInterceptor {

  constructor() { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let userId = "Admin";
    let password = "VerySecretPwd";

    let authHeader = "Basic " + window.btoa(userId + ":" + password);

    request = request.clone(
      {
        setHeaders : { Authorization: authHeader }
      });

    return next.handle(request);
  }
}
