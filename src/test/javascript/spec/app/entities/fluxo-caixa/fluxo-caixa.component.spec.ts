import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { CinematicaTestModule } from '../../../test.module';
import { FluxoCaixaComponent } from 'app/entities/fluxo-caixa/fluxo-caixa.component';
import { FluxoCaixaService } from 'app/entities/fluxo-caixa/fluxo-caixa.service';
import { FluxoCaixa } from 'app/shared/model/fluxo-caixa.model';

describe('Component Tests', () => {
  describe('FluxoCaixa Management Component', () => {
    let comp: FluxoCaixaComponent;
    let fixture: ComponentFixture<FluxoCaixaComponent>;
    let service: FluxoCaixaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [FluxoCaixaComponent],
        providers: []
      })
        .overrideTemplate(FluxoCaixaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FluxoCaixaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FluxoCaixaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new FluxoCaixa(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fluxoCaixas && comp.fluxoCaixas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
