import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ArticoliDataService } from '../services/data/articoli-data.service';

export class Articoli {
  constructor(
    public codArt : String,
    public descrizione : string,
    public um : string,
    public pzCart : number,
    public pesoNetto : number,
    public prezzo : number,
    public idStatoArt : boolean,
    public dataCreaz : Date
  ) {}
}

@Component({
  selector: 'app-articoli',
  templateUrl: './articoli.component.html',
  styleUrls: ['./articoli.component.css']
})
export class ArticoliComponent implements OnInit {

  articolo: Articoli;
  articoli: Articoli[];
  /*
  articoli = [
    new Articoli('014600301', 'BARILLA FARINA 1 KG', 'PZ', 24, 1, 1.09, true, new Date()),
    new Articoli("013500121", "BARILLA PASTA GR.500 N.70 1/2 PENNE", "PZ", 30, 0.5, 1.3, false, new Date()),
    new Articoli("007686402", "FINDUS FIOR DI NASELLO 300 GR", "PZ", 8, 0.3, 6.46, false, new Date()),
    new Articoli("057549001", "FINDUS CROCCOLE 400 GR", "PZ", 12, 0.4, 5.97, false, new Date())
  ]
  */
  numArt = 0;//this.articoli.length;
  pagina = 1;
  righe = 10;
  filter: string = '';

  constructor(private route : ActivatedRoute, private articoliService: ArticoliDataService) { }

  ngOnInit(): void {
    this.filter = this.route.snapshot.params['filter'];
    if (this.filter != undefined)
      this.getArticoli(this.filter);
  }

  refresh() {
    this.getArticoli(this.filter);
  }

  getArticoli(filter: string) {
    console.log("Ricerchiamo articoli per codart con filtro " + filter);
    this.articoliService.getArticoliByCodArt(filter).subscribe(
      response => {
        this.updateModelByArticolo(response, filter);
        this.updateNumArt();
      },
      error => {
        //console.log(error);
        console.log(error.error.messaggio);
        console.log("Ricerchiamo articoli per descrizione con filtro " + filter);

        this.articoliService.getArticoliByDescription(filter).subscribe(
          response => {
            this.updateModelByArticoli(response, filter);
            this.updateNumArt();
          },
          error => {
            //console.log(error);
            console.log(error.error.messaggio);
            console.log("Ricerchiamo articoli per EAN con filtro " + filter);

            this.articoliService.getArticoliByEan(filter).subscribe(
              response => {
                this.updateModelByArticolo(response, filter);
                this.updateNumArt();
              },
              error => {
                this.articoli = [];
                //console.log(error);
                console.log(error.error.messaggio);
              }
            );
          }
        );
      }
    );
  }

  updateModelByArticolo(response: Articoli, filter: string) {
    this.articoli = [];
    this.articolo = response;
    console.log(response);
    this.articoli.push(this.articolo);
  }

  updateModelByArticoli(response: Articoli[], filter: string) {
    this.articoli = response;
    console.log(response);
  }

  updateNumArt() {
    this.numArt = this.articoli.length;
    console.log(this.articoli.length);
  }
}
