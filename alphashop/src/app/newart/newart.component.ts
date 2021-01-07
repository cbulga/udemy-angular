import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Articoli, FamAssort, Ingredienti, Iva } from '../articoli/articoli.component';
import { ArticoliDataService } from '../services/data/articoli-data.service';

@Component({
  selector: 'app-newart',
  templateUrl: './newart.component.html',
  styleUrls: ['./newart.component.css']
})
export class NewartComponent implements OnInit {

  codArt: string = '';
  articolo: Articoli;
  conferma: string = '';
  errore: string = '';
  isModifica: boolean = false;
  ivas: Iva;
  famAssorts: FamAssort;

  constructor(private route: ActivatedRoute, private router: Router, private articoliService: ArticoliDataService) { }

  ngOnInit(): void {
    // empty Articolo initialization to prevent errors like "core.js:5967 ERROR TypeError: Cannot read property 'idIva' of null" happening before the remote Articoli has been loaded
    this.articolo = new Articoli('-1', null, null, null, 0, 0, 0, "1", new Date(), null, new FamAssort(null, null), null, new Iva(null, null, null));

    //Otteniamo i dati della famiglia assortimento
    this.articoliService.getFamAssort().subscribe(
      response => {
        this.famAssorts = response;
        console.log(response);
      },
      error => {
        console.log(error);
      }
    )

    //Otteniamo i dati dell'Iva
    this.articoliService.getIva().subscribe(
      response => {
        this.ivas = response;
        console.log(response);
      },
      error => {
        console.log(error);
      }
    )

    this.codArt = this.route.snapshot.params['codArt'];
    this.isModifica = this.codArt != '-1';
    if (this.isModifica) {
      this.articoliService.getArticoliByCodArt(this.codArt).subscribe(
        response => {
          this.articolo = response;
          console.log(this.articolo);
        },
        error => {
          console.log(error.error.messaggio);
        }
      );
    }
  }

  abort(): void {
    this.router.navigate(["articoli", this.articolo.codArt]);
  }

  salva() {
    this.conferma = '';
    this.errore = '';

    if (this.isModifica) {
      console.log(`Modifica dell'articolo con codart ${this.articolo.codArt}`);
      this.articoliService.updArticoli(this.articolo).subscribe(
        response => {
          console.log(response);
          this.conferma = response.message;
          this.router.navigate(["newart", this.articolo.codArt]);
        },
        error => {
          console.log(error.error.messaggio);
          this.errore = error.error.messaggio;
        }
      );
    } else {
      console.log(`Inserimento dell'articolo con codart ${this.articolo.codArt}`);
      this.articoliService.insArticoli(this.articolo).subscribe(
        response => {
          console.log(response);
          this.conferma = response.message;
        },
        error => {
          console.log(error.error.messaggio);
          this.errore = error.error.messaggio;
        }
      );
    }
  }
}
