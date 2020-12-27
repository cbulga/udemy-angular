import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthappService } from '../services/authapp.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userid = '';
  password = '';
  autenticato = false
  loginButtonClicked = false;
  errorMsg = 'Spiacenti, la userid o la password sono errati!'

  constructor(private route : Router, private authService : AuthappService) { }

  ngOnInit(): void {
  }

  gestAut(): void {
    this.loginButtonClicked = true;
    this.autenticato = this.authService.login(this.userid, this.password);
    if (this.autenticato)
      this.route.navigate(["welcome", this.userid]);
  }
}
