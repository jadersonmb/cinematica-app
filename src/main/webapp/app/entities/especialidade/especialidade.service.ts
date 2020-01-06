import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IEspecialidade } from 'app/shared/model/especialidade.model';

type EntityResponseType = HttpResponse<IEspecialidade>;
type EntityArrayResponseType = HttpResponse<IEspecialidade[]>;

@Injectable({ providedIn: 'root' })
export class EspecialidadeService {
  public resourceUrl = SERVER_API_URL + 'api/especialidades';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/especialidades';

  constructor(protected http: HttpClient) {}

  create(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.post<IEspecialidade>(this.resourceUrl, especialidade, { observe: 'response' });
  }

  update(especialidade: IEspecialidade): Observable<EntityResponseType> {
    return this.http.put<IEspecialidade>(this.resourceUrl, especialidade, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEspecialidade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspecialidade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspecialidade[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
