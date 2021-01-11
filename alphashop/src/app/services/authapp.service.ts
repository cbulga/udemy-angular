import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {map} from 'rxjs/operators';
import { port, server } from '../app.constants';

export class AuthData {

  constructor(
    public codice: string,
    public messaggio: string
  ) {}
}

@Injectable({
  providedIn: 'root'
})
export class AuthappService {

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

  autenticaService(userId: string, password: string) {
    let authString = "Basic " + window.btoa(userId + ":" + password);
    let headers = new HttpHeaders(
      {Authorization: authString}
    )

    return this.httpClient.get<AuthData>(
      `http://${server}:${port}/api/articoli/test`,
      {headers})
      .pipe(
        map(
          data => {
            sessionStorage.setItem("Utente", userId);
            sessionStorage.setItem("AuthToken", authString);
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
