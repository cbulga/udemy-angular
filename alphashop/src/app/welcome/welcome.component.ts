import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SalutiDataService } from '../services/data/saluti-data.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  saluti: string = 'Benvenuti in Alphashop';
  titolo2 = 'Seleziona gli articoli da acquistare'
  utente = ''
  messaggio = ''

  constructor(private route : ActivatedRoute, private salutiService: SalutiDataService) { }

  ngOnInit(): void {
    this.utente = this.route.snapshot.params['userid'];
  }

  getSaluti() {
    console.log(this.messaggio);
    this.salutiService.getSaluti().subscribe(
      response => this.handleResponse(response)
    );
  }

  handleResponse(response: any) {
    this.messaggio = response;
  }
}
