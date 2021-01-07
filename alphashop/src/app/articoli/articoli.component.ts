import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticoliDataService } from '../services/data/articoli-data.service';

export class Articoli {
  constructor(
    public codArt: string,
    public descrizione: string,
    public um: string,
    public codStat: string,
    public pzCart: number,
    public pesoNetto: number,
    public prezzo: number,
    public idStatoArt: string,
    public dataCreaz: Date,
    public barcode: Barcode,
    public famAssort: FamAssort,
    public ingredienti: Ingredienti,
    public iva: Iva
  ) {}
}

export class Barcode {

  constructor(
    public barcode: string,
    public idTipoArt: string
  ) {}
}

export class Ingredienti {

  constructor(
    public codart: string,
    public info: string
  ) {}
}

export class FamAssort {

  constructor(
    public id: number,
    public descrizione: string
  ) {}
}

export class Iva {

  constructor(
    public idIva: number,
    public descrizione: string,
    public aliquota: number
  ) {}
}

export class ApiMsg {
  constructor(public code: string,
    public message: string) {}
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
  apiMsg: ApiMsg;
  messaggio: string;

  constructor(private route : ActivatedRoute, private router: Router, private articoliService: ArticoliDataService) { }

  ngOnInit(): void {
    this.filter = this.route.snapshot.params['filter'];
    if (this.filter != undefined)
      this.getArticoli(this.filter);
  }

  refresh() {
    this.getArticoli(this.filter);
  }

  getArticoli(filter: string) {
    console.log("Ricerchiamo articoli per codArt con filtro " + filter);
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

  elimina = (codArt: string) => {
    console.log(`Eliminiamo l'articolo con codArt ${codArt}`);
    this.articoliService.deleteArticoliByCodArt(codArt).subscribe(
      response => {
        console.log(response);
        this.apiMsg = response;
        this.messaggio = this.apiMsg.message;
        this.refresh();
      },
      error => {
        console.log(error.error.messaggio);
        this.messaggio = error.error.messaggio;
      }
    );
  }

  modifica = (codArt: string) => {
    console.log(`Modifica articolo con codArt ${codArt}`);
    this.router.navigate(['/newart', codArt]);
  }
}
