import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IProfissao } from 'app/shared/model/profissao.model';

type EntityResponseType = HttpResponse<IProfissao>;
type EntityArrayResponseType = HttpResponse<IProfissao[]>;

@Injectable({ providedIn: 'root' })
export class ProfissaoService {
  public resourceUrl = SERVER_API_URL + 'api/profissaos';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/profissaos';

  constructor(protected http: HttpClient) {}

  create(profissao: IProfissao): Observable<EntityResponseType> {
    return this.http.post<IProfissao>(this.resourceUrl, profissao, { observe: 'response' });
  }

  update(profissao: IProfissao): Observable<EntityResponseType> {
    return this.http.put<IProfissao>(this.resourceUrl, profissao, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProfissao>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfissao[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProfissao[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
