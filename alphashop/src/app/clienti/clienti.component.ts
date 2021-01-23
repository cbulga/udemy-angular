import { Component, OnInit } from '@angular/core';
import { IClienti } from '../interfaces';

@Component({
  selector: 'app-clienti',
  templateUrl: './clienti.component.html',
  styleUrls: ['./clienti.component.css']
})
export class ClientiComponent implements OnInit {

  clienti: IClienti[] = [
    {
      nome: "Cristian Bulgarelli",
      bollini: 948,
      attivo: true,
      idFid: "48000123"
    },
    {
      nome: "Monica Barbieri",
      bollini: 1500,
      attivo: true,
      idFid: "48000124"
    },
    {
      nome: "Sky Bulgarelli",
      bollini: 6000,
      attivo: false,
      idFid: "48000125"
    },
  ];
  testCli: IClienti | undefined;

  constructor() { }

  ngOnInit(): void {
  }

  checkStatus(idFid: string) {
    console.log(idFid);
    this.testCli = this.clienti.find(x => x.idFid == idFid);

    if (this.testCli?.attivo) {
      if (this.testCli.bollini >= 1000) {
        window.alert(`Il cliente ${this.testCli?.nome} può ritirare il suo buono`);
      } else {
        window.alert(`Il cliente ${this.testCli?.nome} deve ancora ottenere ${(this.testCli?.bollini - 1000) * -1} bollini`);
      }
    } else {
      window.alert(`Il cliente ${this.testCli?.nome} non è attivo!`);
    }
  }
}
