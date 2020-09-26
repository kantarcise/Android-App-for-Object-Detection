a=VideoReader('C:\Users\%USERNAME%\Desktop\%video.avi%');

%%For the number of frames you want, adjust the loop.
for img = 1:150;
    filename=strcat(num2str(img),'.jpg');
    b = read(a, img);
    imwrite(b,filename);
end