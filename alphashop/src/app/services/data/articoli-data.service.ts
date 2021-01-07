import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Articoli, ApiMsg } from 'src/app/articoli/articoli.component';

@Injectable({
  providedIn: 'root'
})
export class ArticoliDataService {

  server = 'localhost';
  port = '5051';

  constructor(private httpClient: HttpClient) { }

  getArticoliByDescription = (descrizione: string) => this.httpClient.get<Articoli[]>(`http://${this.server}:${this.port}/api/articoli/cerca/descrizione/${descrizione}`);

  getArticoliByCodArt(codArt: string) {
    return this.httpClient.get<Articoli>(`http://${this.server}:${this.port}/api/articoli/cerca/codice/${codArt}`);
  }

  getArticoliByEan(barcode: string) {
    return this.httpClient.get<Articoli>(`http://${this.server}:${this.port}/api/articoli/cerca/ean/${barcode}`);
  }

  deleteArticoliByCodArt(codArt: string) {
    return this.httpClient.delete<ApiMsg>(`http://${this.server}:${this.port}/api/articoli/elimina/${codArt}`);
  }

  updArticoli(articoli: Articoli) {
    return this.httpClient.put<ApiMsg>(`http://${this.server}:${this.port}/api/articoli/modifica`, articoli);
  }

  insArticoli(articoli: Articoli) {
    return this.httpClient.post<ApiMsg>(`http://${this.server}:${this.port}/api/articoli/inserisci`, articoli);
  }
}
