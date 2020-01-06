import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, Search } from 'app/shared/util/request-util';
import { IFluxoCaixa } from 'app/shared/model/fluxo-caixa.model';

type EntityResponseType = HttpResponse<IFluxoCaixa>;
type EntityArrayResponseType = HttpResponse<IFluxoCaixa[]>;

@Injectable({ providedIn: 'root' })
export class FluxoCaixaService {
  public resourceUrl = SERVER_API_URL + 'api/fluxo-caixas';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/fluxo-caixas';

  constructor(protected http: HttpClient) {}

  create(fluxoCaixa: IFluxoCaixa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fluxoCaixa);
    return this.http
      .post<IFluxoCaixa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fluxoCaixa: IFluxoCaixa): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fluxoCaixa);
    return this.http
      .put<IFluxoCaixa>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFluxoCaixa>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFluxoCaixa[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFluxoCaixa[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(fluxoCaixa: IFluxoCaixa): IFluxoCaixa {
    const copy: IFluxoCaixa = Object.assign({}, fluxoCaixa, {
      dataLancamento: fluxoCaixa.dataLancamento && fluxoCaixa.dataLancamento.isValid() ? fluxoCaixa.dataLancamento.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dataLancamento = res.body.dataLancamento ? moment(res.body.dataLancamento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fluxoCaixa: IFluxoCaixa) => {
        fluxoCaixa.dataLancamento = fluxoCaixa.dataLancamento ? moment(fluxoCaixa.dataLancamento) : undefined;
      });
    }
    return res;
  }
}
