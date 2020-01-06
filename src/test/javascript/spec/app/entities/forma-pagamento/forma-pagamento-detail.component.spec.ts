import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { FormaPagamentoDetailComponent } from 'app/entities/forma-pagamento/forma-pagamento-detail.component';
import { FormaPagamento } from 'app/shared/model/forma-pagamento.model';

describe('Component Tests', () => {
  describe('FormaPagamento Management Detail Component', () => {
    let comp: FormaPagamentoDetailComponent;
    let fixture: ComponentFixture<FormaPagamentoDetailComponent>;
    const route = ({ data: of({ formaPagamento: new FormaPagamento(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [FormaPagamentoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FormaPagamentoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FormaPagamentoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load formaPagamento on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.formaPagamento).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
