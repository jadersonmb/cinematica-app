import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IConfiguracaoAgenda } from 'app/shared/model/configuracao-agenda.model';

type EntityResponseType = HttpResponse<IConfiguracaoAgenda>;
type EntityArrayResponseType = HttpResponse<IConfiguracaoAgenda[]>;

@Injectable({ providedIn: 'root' })
export class ConfiguracaoAgendaService {
  public resourceUrl = SERVER_API_URL + 'api/configuracao-agenda';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/configuracao-agenda';

  constructor(protected http: HttpClient) {}

  create(configuracaoAgenda: IConfiguracaoAgenda): Observable<EntityResponseType> {
    return this.http.post<IConfiguracaoAgenda>(this.resourceUrl, configuracaoAgenda, { observe: 'response' });
  }

  update(configuracaoAgenda: IConfiguracaoAgenda): Observable<EntityResponseType> {
    return this.http.put<IConfiguracaoAgenda>(this.resourceUrl, configuracaoAgenda, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfiguracaoAgenda>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfiguracaoAgenda[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfiguracaoAgenda[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
