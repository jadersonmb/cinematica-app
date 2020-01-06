import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { FluxoCaixaService } from 'app/entities/fluxo-caixa/fluxo-caixa.service';
import { IFluxoCaixa, FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';
import { TipoLancamento } from 'app/shared/model/enumerations/tipo-lancamento.model';

describe('Service Tests', () => {
  describe('FluxoCaixa Service', () => {
    let injector: TestBed;
    let service: FluxoCaixaService;
    let httpMock: HttpTestingController;
    let elemDefault: IFluxoCaixa;
    let expectedResult: IFluxoCaixa | IFluxoCaixa[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FluxoCaixaService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new FluxoCaixa(0, currentDate, 'AAAAAAA', 0, TipoLancamento.Receita, 'AAAAAAA', 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataLancamento: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FluxoCaixa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataLancamento: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataLancamento: currentDate
          },
          returnedFromService
        );
        service
          .create(new FluxoCaixa())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FluxoCaixa', () => {
        const returnedFromService = Object.assign(
          {
            dataLancamento: currentDate.format(DATE_TIME_FORMAT),
            descricao: 'BBBBBB',
            valor: 1,
            tipoLancamento: 'BBBBBB',
            numeroRecibo: 'BBBBBB',
            quantidadeParcela: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataLancamento: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FluxoCaixa', () => {
        const returnedFromService = Object.assign(
          {
            dataLancamento: currentDate.format(DATE_TIME_FORMAT),
            descricao: 'BBBBBB',
            valor: 1,
            tipoLancamento: 'BBBBBB',
            numeroRecibo: 'BBBBBB',
            quantidadeParcela: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataLancamento: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FluxoCaixa', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
