import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthappService {

  constructor() { }

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

  /**
   * Returns the currently logged user; null otherwise.
   */
  loggedUser = (): string => sessionStorage.getItem("Utente") != null ? sessionStorage.getItem("Utente") : "";

  /**
   * Logs out the current user.
   */
  logout = () => sessionStorage.removeItem("Utente");
}
