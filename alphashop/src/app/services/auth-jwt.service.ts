import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {map} from 'rxjs/operators';
import { authServerUri, port, server } from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class AuthJWTService {

  constructor(private httpClient: HttpClient) { }
/*
  server = "localhost";
  port = "5051";
*/

  login = (userId: string, password: string): boolean => {
    var result = userId === 'Cristian' && password === '123_Stella';
    if (result)
      sessionStorage.setItem("Utente", userId);
    return result;
  }

  /**
   * Returns true in case the current user is correctly logged in; false otherwise.
   */
  isLoggedIn = (): boolean => sessionStorage.getItem("Utente") != null;

  autenticaService(username: string, password: string) {
    return this.httpClient.post<any>(
      `${authServerUri}`,
      {username, password})
      .pipe(
        map(
          data => {
            sessionStorage.setItem("Utente", username);
            sessionStorage.setItem("AuthToken", `Bearer ${data.token}`);
            return data;
          }
        )
      );
  }

  /**
   * Returns the currently logged user; null otherwise.
   */
  loggedUser = (): string => sessionStorage.getItem("Utente") != null ? sessionStorage.getItem("Utente") : "";

  getAuthToken() {
    if (this.loggedUser())
      return sessionStorage.getItem("AuthToken");
    else
      return "";
  }

  /**
   * Logs out the current user.
   */
  logout = () => sessionStorage.removeItem("Utente");
}
