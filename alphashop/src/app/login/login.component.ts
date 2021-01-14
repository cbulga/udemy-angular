import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthJWTService } from '../services/auth-jwt.service';

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

  constructor(private route : Router, private authService : AuthJWTService) { }

  ngOnInit(): void {
  }

  gestAut(): void {
    this.loginButtonClicked = true;

    this.authService.autenticaService(this.userid, this.password).subscribe(
      data => {
        console.log(data);
        this.autenticato = true;
        this.route.navigate(["welcome", this.userid]);
      },
      error => {
        console.log(error);
        this.autenticato = false;
      }
    );
/*
    this.autenticato = this.authService.login(this.userid, this.password);
    if (this.autenticato)
      this.route.navigate(["welcome", this.userid]);
*/
  }
}
