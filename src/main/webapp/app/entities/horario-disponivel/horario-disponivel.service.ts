import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IHorarioDisponivel } from 'app/shared/model/horario-disponivel.model';

type EntityResponseType = HttpResponse<IHorarioDisponivel>;
type EntityArrayResponseType = HttpResponse<IHorarioDisponivel[]>;

@Injectable({ providedIn: 'root' })
export class HorarioDisponivelService {
  public resourceUrl = SERVER_API_URL + 'api/horario-disponivels';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/horario-disponivels';

  constructor(protected http: HttpClient) {}

  create(horarioDisponivel: IHorarioDisponivel): Observable<EntityResponseType> {
    return this.http.post<IHorarioDisponivel>(this.resourceUrl, horarioDisponivel, { observe: 'response' });
  }

  update(horarioDisponivel: IHorarioDisponivel): Observable<EntityResponseType> {
    return this.http.put<IHorarioDisponivel>(this.resourceUrl, horarioDisponivel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHorarioDisponivel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHorarioDisponivel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHorarioDisponivel[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
