import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CinematicaTestModule } from '../../../test.module';
import { MedicoDetailComponent } from 'app/entities/medico/medico-detail.component';
import { Medico } from 'app/shared/model/medico.model';

describe('Component Tests', () => {
  describe('Medico Management Detail Component', () => {
    let comp: MedicoDetailComponent;
    let fixture: ComponentFixture<MedicoDetailComponent>;
    const route = ({ data: of({ medico: new Medico(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [CinematicaTestModule],
        declarations: [MedicoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MedicoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MedicoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load medico on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.medico).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
