import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PacienteService } from 'app/entities/paciente/paciente.service';
import { IPaciente, Paciente } from 'app/shared/model/paciente.model';
import { Sexo } from 'app/shared/model/enumerations/sexo.model';

describe('Service Tests', () => {
  describe('Paciente Service', () => {
    let injector: TestBed;
    let service: PacienteService;
    let httpMock: HttpTestingController;
    let elemDefault: IPaciente;
    let expectedResult: IPaciente | IPaciente[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PacienteService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Paciente(
        0,
        'AAAAAAA',
        'AAAAAAA',
        currentDate,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        false,
        Sexo.Masculino
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            criadoEm: currentDate.format(DATE_TIME_FORMAT),
            atualizadoEm: currentDate.format(DATE_TIME_FORMAT),
            dataNascimento: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Paciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            criadoEm: currentDate.format(DATE_TIME_FORMAT),
            atualizadoEm: currentDate.format(DATE_TIME_FORMAT),
            dataNascimento: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            criadoEm: currentDate,
            atualizadoEm: currentDate,
            dataNascimento: currentDate
          },
          returnedFromService
        );
        service
          .create(new Paciente())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Paciente', () => {
        const returnedFromService = Object.assign(
          {
            nome: 'BBBBBB',
            nomeCompleto: 'BBBBBB',
            criadoEm: currentDate.format(DATE_TIME_FORMAT),
            atualizadoEm: currentDate.format(DATE_TIME_FORMAT),
            cpf: 'BBBBBB',
            rg: 'BBBBBB',
            email: 'BBBBBB',
            telefoneCelular: 'BBBBBB',
            fotoUrl: 'BBBBBB',
            funcionario: true,
            dataNascimento: currentDate.format(DATE_TIME_FORMAT),
            crefito: 'BBBBBB',
            telefone: 'BBBBBB',
            fotoUrlEndereco: 'BBBBBB',
            indicacao: 'BBBBBB',
            ativo: true,
            sexo: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            criadoEm: currentDate,
            atualizadoEm: currentDate,
            dataNascimento: currentDate
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

      it('should return a list of Paciente', () => {
        const returnedFromService = Object.assign(
          {
            nome: 'BBBBBB',
            nomeCompleto: 'BBBBBB',
            criadoEm: currentDate.format(DATE_TIME_FORMAT),
            atualizadoEm: currentDate.format(DATE_TIME_FORMAT),
            cpf: 'BBBBBB',
            rg: 'BBBBBB',
            email: 'BBBBBB',
            telefoneCelular: 'BBBBBB',
            fotoUrl: 'BBBBBB',
            funcionario: true,
            dataNascimento: currentDate.format(DATE_TIME_FORMAT),
            crefito: 'BBBBBB',
            telefone: 'BBBBBB',
            fotoUrlEndereco: 'BBBBBB',
            indicacao: 'BBBBBB',
            ativo: true,
            sexo: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            criadoEm: currentDate,
            atualizadoEm: currentDate,
            dataNascimento: currentDate
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

      it('should delete a Paciente', () => {
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
