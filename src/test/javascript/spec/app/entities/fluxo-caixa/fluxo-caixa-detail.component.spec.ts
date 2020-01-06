import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { FluxoCaixaDetailComponent } from 'app/entities/fluxo-caixa/fluxo-caixa-detail.component';
import { FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';

describe('Component Tests', () => {
  describe('FluxoCaixa Management Detail Component', () => {
    let comp: FluxoCaixaDetailComponent;
    let fixture: ComponentFixture<FluxoCaixaDetailComponent>;
    const route = ({ data: of({ fluxoCaixa: new FluxoCaixa(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [FluxoCaixaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FluxoCaixaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FluxoCaixaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fluxoCaixa on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fluxoCaixa).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
