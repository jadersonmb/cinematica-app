import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { FormaPagamentoComponent } from 'app/entities/forma-pagamento/forma-pagamento.component';
import { FormaPagamentoService } from 'app/entities/forma-pagamento/forma-pagamento.service';
import { FormaPagamento } from 'app/shared/model/forma-pagamento.model';

describe('Component Tests', () => {
  describe('FormaPagamento Management Component', () => {
    let comp: FormaPagamentoComponent;
    let fixture: ComponentFixture<FormaPagamentoComponent>;
    let service: FormaPagamentoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [FormaPagamentoComponent],
        providers: []
      })
        .overrideTemplate(FormaPagamentoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FormaPagamentoComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FormaPagamentoService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FormaPagamento(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.formaPagamentos && comp.formaPagamentos[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
