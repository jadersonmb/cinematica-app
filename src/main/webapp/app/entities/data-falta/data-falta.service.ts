import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IDataFalta } from 'app/shared/model/data-falta.model';

type EntityResponseType = HttpResponse<IDataFalta>;
type EntityArrayResponseType = HttpResponse<IDataFalta[]>;

@Injectable({ providedIn: 'root' })
export class DataFaltaService {
  public resourceUrl = SERVER_API_URL + 'api/data-faltas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/data-faltas';

  constructor(protected http: HttpClient) {}

  create(dataFalta: IDataFalta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataFalta);
    return this.http
      .post<IDataFalta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dataFalta: IDataFalta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dataFalta);
    return this.http
      .put<IDataFalta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDataFalta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataFalta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDataFalta[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(dataFalta: IDataFalta): IDataFalta {
    const copy: IDataFalta = Object.assign({}, dataFalta, {
      dataFalta: dataFalta.dataFalta && dataFalta.dataFalta.isValid() ? dataFalta.dataFalta.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataFalta = res.body.dataFalta ? moment(res.body.dataFalta) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dataFalta: IDataFalta) => {
        dataFalta.dataFalta = dataFalta.dataFalta ? moment(dataFalta.dataFalta) : undefined;
      });
    }
    return res;
  }
}
