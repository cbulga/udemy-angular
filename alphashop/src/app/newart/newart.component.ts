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

  ivas = [{id: 22, descrizione: 'Iva 22%', aliquota: 22},
    {id: 10, descrizione: 'Iva 10%', aliquota: 10},
    {id: 4, descrizione: 'Iva 4%', aliquota: 4},
    {id: 0, descrizione: 'Iva esente', aliquota: 0}
  ];

  FamAssort = [
    {
      id: -1,
      descrizione: "NON DISPONIBILE"
    },
    {
      id: 1,
      descrizione: "DROGHERIA ALIMENTARE"
    },
    {
      id: 10,
      descrizione: "DROGHERIA CHIMICA"
    },
    {
      id: 15,
      descrizione: "BANCO TAGLIO"
    },
    {
      id: 16,
      descrizione: "GASTRONOMIA"
    },
    {
      id: 17,
      descrizione: "PASTECCERIA"
    },
    {
      id: 20,
      descrizione: "LIBERO SERVIZIO"
    },
    {
      id: 25,
      descrizione: "PANE"
    },
    {
      id: 40,
      descrizione: "SURGELATI"
    },
    {
      id: 50,
      descrizione: "ORTOFRUTTA"
    },
    {
      id: 60,
      descrizione: "MACELLERIA"
    },
    {
      id: 70,
      descrizione: "PESCHERIA"
    },
    {
      id: 90,
      descrizione: "EXTRA ALIMENTARI"
    }

  ]

  constructor(private route: ActivatedRoute, private router: Router,  private articoliService: ArticoliDataService) { }

  ngOnInit(): void {
    // empty Articolo initialization to prevent errors like "core.js:5967 ERROR TypeError: Cannot read property 'idIva' of null" happening before the remote Articoli has been loaded
    this.articolo = new Articoli('-1', null, null, null, 0, 0, 0, "1", new Date(), null, new FamAssort(null, null), null, new Iva(null, null, null));

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
