import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { HorarioDisponivelDetailComponent } from 'app/entities/horario-disponivel/horario-disponivel-detail.component';
import { HorarioDisponivel } from 'app/shared/model/horario-disponivel.model';

describe('Component Tests', () => {
  describe('HorarioDisponivel Management Detail Component', () => {
    let comp: HorarioDisponivelDetailComponent;
    let fixture: ComponentFixture<HorarioDisponivelDetailComponent>;
    const route = ({ data: of({ horarioDisponivel: new HorarioDisponivel(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [HorarioDisponivelDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(HorarioDisponivelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(HorarioDisponivelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load horarioDisponivel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.horarioDisponivel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
