import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { AgendaDetailComponent } from 'app/entities/agenda/agenda-detail.component';
import { Agenda } from 'app/shared/model/agenda.model';

describe('Component Tests', () => {
  describe('Agenda Management Detail Component', () => {
    let comp: AgendaDetailComponent;
    let fixture: ComponentFixture<AgendaDetailComponent>;
    const route = ({ data: of({ agenda: new Agenda(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [AgendaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AgendaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AgendaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load agenda on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.agenda).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
