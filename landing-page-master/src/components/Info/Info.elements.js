import styled from "styled-components";


export const InfoSec = styled.div`
color: #fff;
padding: 100px 0;
background: #D8DEE9;
display: flex;
height: 100vh;

`;

export const InfoColumn = styled.div`
margin-bottom: 15px;
padding-right: 200px;
padding-left: 200px;
flex: 1;
flex-basis: 50%;
display: flex;   
justify-content: center;

@media screen and (max-width: 768px) {
    padding-right: 50px;
    padding-left: 50px;
    max-width: 100%;
    flex-basis: 100%;
    display: flex;   
    justify-content: center;
}
`;

export const TextWrap = styled.div`
padding-top: 0;
padding-bottom: 60px;
color: #434C5E;
text-align: center;

@media screen and (max-width: 768px) {
    padding-bottom: 65px;
}
`;

export const ErrorWrap = styled.div`
padding-top: 0;
padding-bottom: 60px;
color: #BF616A;
text-align: center;

@media screen and (max-width: 768px) {
    padding-bottom: 65px;
}
`;

export const ImgWrap = styled.div`
padding-top: 0;
margin-top: -70px;
display: flex;
justify-content: center;
`;

export const Img = styled.img`
border:0;
max-width: 75%;
max-height: 75%;
vertical-align: middle;
display: inline-block;
margin-bottom: -20px;

@media screen and (max-width: 768px) {
    
    max-width: 100%;
    max-height: 100%;
}
`;
