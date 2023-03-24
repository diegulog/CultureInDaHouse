export interface IShow {
    id:          number;
    name:        string;
    description: string;
    image:       string;
    price:       number;
    duration:    number;
    capacity:    number;
    onSaleDate:  Date;
    status:      string;
    category:    ICategory;
}

export interface ICategory {
    id:          number;
    name:        string;
    description: string;
}