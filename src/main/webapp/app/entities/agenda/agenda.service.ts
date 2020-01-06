import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IAgenda } from 'app/shared/model/agenda.model';

type EntityResponseType = HttpResponse<IAgenda>;
type EntityArrayResponseType = HttpResponse<IAgenda[]>;

@Injectable({ providedIn: 'root' })
export class AgendaService {
  public resourceUrl = SERVER_API_URL + 'api/agenda';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/agenda';

  constructor(protected http: HttpClient) {}

  create(agenda: IAgenda): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agenda);
    return this.http
      .post<IAgenda>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(agenda: IAgenda): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(agenda);
    return this.http
      .put<IAgenda>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAgenda>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgenda[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAgenda[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(agenda: IAgenda): IAgenda {
    const copy: IAgenda = Object.assign({}, agenda, {
      dataInicio: agenda.dataInicio && agenda.dataInicio.isValid() ? agenda.dataInicio.toJSON() : undefined,
      dataFim: agenda.dataFim && agenda.dataFim.isValid() ? agenda.dataFim.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataInicio = res.body.dataInicio ? moment(res.body.dataInicio) : undefined;
      res.body.dataFim = res.body.dataFim ? moment(res.body.dataFim) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((agenda: IAgenda) => {
        agenda.dataInicio = agenda.dataInicio ? moment(agenda.dataInicio) : undefined;
        agenda.dataFim = agenda.dataFim ? moment(agenda.dataFim) : undefined;
      });
    }
    return res;
  }
}
