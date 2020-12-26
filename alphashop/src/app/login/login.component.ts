import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userid = '';
  password = '';
  autenticato = false
  errorMsg = 'Spiacenti, la userid o la password sono errati!'

  constructor(private route : Router) { }

  ngOnInit(): void {
  }

  gestAut(): void {
    this.autenticato = this.userid === 'Cristian' && this.password === '123_Stella';
    if (this.autenticato)
      this.route.navigate(["welcome", this.userid]);
  }
}
