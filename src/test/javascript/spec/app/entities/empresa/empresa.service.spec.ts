import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { EmpresaService } from 'app/entities/empresa/empresa.service';
import { IEmpresa, Empresa } from 'app/shared/model/empresa.model';

describe('Service Tests', () => {
  describe('Empresa Service', () => {
    let injector: TestBed;
    let service: EmpresaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmpresa;
    let expectedResult: IEmpresa | IEmpresa[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(EmpresaService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Empresa(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataContratacao: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Empresa', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataContratacao: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataContratacao: currentDate
          },
          returnedFromService
        );
        service
          .create(new Empresa())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Empresa', () => {
        const returnedFromService = Object.assign(
          {
            nomeFantasia: 'BBBBBB',
            razaoSocial: 'BBBBBB',
            cnpj: 'BBBBBB',
            telefone: 'BBBBBB',
            email: 'BBBBBB',
            inscricaoEstadual: 'BBBBBB',
            inscricaoMunicipal: 'BBBBBB',
            website: 'BBBBBB',
            dataContratacao: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataContratacao: currentDate
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

      it('should return a list of Empresa', () => {
        const returnedFromService = Object.assign(
          {
            nomeFantasia: 'BBBBBB',
            razaoSocial: 'BBBBBB',
            cnpj: 'BBBBBB',
            telefone: 'BBBBBB',
            email: 'BBBBBB',
            inscricaoEstadual: 'BBBBBB',
            inscricaoMunicipal: 'BBBBBB',
            website: 'BBBBBB',
            dataContratacao: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dataContratacao: currentDate
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

      it('should delete a Empresa', () => {
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
