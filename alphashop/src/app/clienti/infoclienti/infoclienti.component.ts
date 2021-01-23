import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-infoclienti',
  templateUrl: './infoclienti.component.html',
  styleUrls: ['./infoclienti.component.css']
})
export class InfoclientiComponent implements OnInit {

  @Input() nome: string = "";
  @Input() bollini: number = 0;
  @Input() attivo: boolean = true;
  @Input() idFid: string = "";

  @Output() verifica = new EventEmitter<string>();

  differenza: number = 0;

  constructor() { }

  ngOnInit(): void {
    this.differenza = 1000 - this.bollini;
  }

}
