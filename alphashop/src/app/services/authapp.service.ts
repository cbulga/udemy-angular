import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthappService {

  constructor() { }

  login(userId, password) {
    return userId === 'Cristian' && password === '123_Stella';
  }
}
