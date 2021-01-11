import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { port, server } from 'src/app/app.constants';
import { Articoli, ApiMsg, FamAssort, Iva } from 'src/app/articoli/articoli.component';

@Injectable({
  providedIn: 'root'
})
export class ArticoliDataService {
/*
  server = 'localhost';
  port = '5051';
*/

  constructor(private httpClient: HttpClient) { }

  /*
  getBasicAuthHeader() {
    let userId = "Nicola";
    let password = "123Stella";
    let retVal = "Basic " + window.btoa(userId + ":" + password);
    return retVal;
  }
  */

  getArticoliByDescription(descrizione: string) {
    /*
    let headers = new HttpHeaders(
      {Authorization : this.getBasicAuthHeader() }
    )
    */
    //return this.httpClient.get<Articoli[]>(`http://${this.server}:${this.port}/api/articoli/cerca/descrizione/${descrizione}`, {headers});
    return this.httpClient.get<Articoli[]>(`http://${server}:${port}/api/articoli/cerca/descrizione/${descrizione}`);
  }

  getArticoliByCodArt(codArt: string) {
    return this.httpClient.get<Articoli>(`http://${server}:${port}/api/articoli/cerca/codice/${codArt}`);
  }

  getArticoliByEan(barcode: string) {
    return this.httpClient.get<Articoli>(`http://${server}:${port}/api/articoli/cerca/ean/${barcode}`);
  }

  deleteArticoliByCodArt(codArt: string) {
    return this.httpClient.delete<ApiMsg>(`http://${server}:${port}/api/articoli/elimina/${codArt}`);
  }

  updArticoli(articoli: Articoli) {
    return this.httpClient.put<ApiMsg>(`http://${server}:${port}/api/articoli/modifica`, articoli);
  }

  insArticoli(articoli: Articoli) {
    return this.httpClient.post<ApiMsg>(`http://${server}:${port}/api/articoli/inserisci`, articoli);
  }

  getFamAssort() {
    return this.httpClient.get<FamAssort>(`http://${server}:${port}/api/categoria/cerca/tutti`);
  }

  getIva() {
    return this.httpClient.get<Iva>(`http://${server}:${port}/api/iva/cerca/tutti`);
  }
}
