import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { AuthJWTService } from './auth-jwt.service';

@Injectable({
  providedIn: 'root'
})
export class RouteGuardService implements CanActivate {

  token : string = '';
  ruoli : string[];

  constructor(private authService: AuthJWTService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    this.token = this.authService.getAuthToken();

    const helper = new JwtHelperService();
    const decodedToken = helper.decodeToken(this.token);
    this.ruoli = decodedToken['authorities'];

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(["login"])
      return false;
    } else {
      if (route.data.roles == null || route.data.roles.length === 0)
        return true;
      else if (this.ruoli.some(r => route.data.roles.includes(r)))
        return true;
      else {
        this.router.navigate(['forbidden']);
        return false;
      }
    }
  }
}
