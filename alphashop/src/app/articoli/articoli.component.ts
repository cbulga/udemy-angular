import { Component, OnInit } from '@angular/core';

export class Articoli {
  constructor(
    public codart : String,
    public descrizione : string,
    public um : string,
    public pzcart : number,
    public peso : number,
    public prezzo : number,
    public isactive : boolean,
    public data : Date
  ) {}
}

@Component({
  selector: 'app-articoli',
  templateUrl: './articoli.component.html',
  styleUrls: ['./articoli.component.css']
})
export class ArticoliComponent implements OnInit {

  articoli = [
    new Articoli('014600301', 'BARILLA FARINA 1 KG', 'PZ', 24, 1, 1.09, true, new Date()),
    new Articoli("013500121", "BARILLA PASTA GR.500 N.70 1/2 PENNE", "PZ", 30, 0.5, 1.3, false, new Date()),
    new Articoli("007686402", "FINDUS FIOR DI NASELLO 300 GR", "PZ", 8, 0.3, 6.46, false, new Date()),
    new Articoli("057549001", "FINDUS CROCCOLE 400 GR", "PZ", 12, 0.4, 5.97, false, new Date())
  ]

  constructor() { }

  ngOnInit(): void {
  }

}
