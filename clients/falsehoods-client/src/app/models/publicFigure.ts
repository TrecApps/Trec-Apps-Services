export class PublicFigure{
    id: Number;
    firstname: string;
    middleNames: string;
    lastName: string;
    approved: number;
};

export class PublicFigureEntry {
	figure: PublicFigure;
    text: string;   
}